# LUMI Backend Docker Guide

## Prerequisites

- Docker Desktop
- Docker Compose v2
- A `.env` file in the project root

Create `.env` from the example file if it does not exist:

```powershell
Copy-Item .env.example .env
```

Review these values in `.env` before starting:

```env
POSTGRES_USER=lumi
POSTGRES_PASSWORD=change_me
POSTGRES_DB=lumi
DATABASE_URL=postgresql+asyncpg://lumi:change_me@timescaledb:5432/lumi
TIMESCALEDB_URL=postgresql://lumi:change_me@timescaledb:5432/lumi

REDIS_URL=redis://redis:6379/0
CELERY_BROKER_URL=redis://redis:6379/1
CELERY_RESULT_BACKEND=redis://redis:6379/2
```

## Start The Project

Build and start all services:

```powershell
docker compose up --build
```

Run in the background:

```powershell
docker compose up --build -d
```

The database initialization runs automatically through the `init-db` service before the API and Celery workers start.

## Services

- `api`: FastAPI backend at `http://localhost:8000`
- `timescaledb`: PostgreSQL + TimescaleDB
- `redis`: cache and Celery broker
- `celery-storage`: async storage worker
- `celery-analytics`: analytics and notification worker
- `celery-beat`: scheduled jobs
- `init-db`: one-shot table, hypertable, and seed initialization

## Verify Startup

Check running containers:

```powershell
docker compose ps
```

Check API health:

```powershell
curl.exe http://localhost:8000/health
```

Expected response:

```json
{"status":"ok"}
```

Open Swagger UI:

```text
http://localhost:8000/docs
```

## Logs

API logs:

```powershell
docker compose logs api --tail=200
```

Storage worker logs:

```powershell
docker compose logs celery-storage --tail=200
```

Analytics worker logs:

```powershell
docker compose logs celery-analytics --tail=200
```

Follow logs live:

```powershell
docker compose logs -f api celery-storage celery-analytics
```

## Database Checks

List tables:

```powershell
docker compose exec -T timescaledb psql -U lumi -d lumi -c "SELECT tablename FROM pg_tables WHERE schemaname='public' ORDER BY tablename;"
```

Check Timescale hypertables:

```powershell
docker compose exec -T timescaledb psql -U lumi -d lumi -c "SELECT hypertable_name FROM timescaledb_information.hypertables ORDER BY hypertable_name;"
```

Check sleep sessions:

```powershell
docker compose exec -T timescaledb psql -U lumi -d lumi -c "SELECT user_id, start_time, end_time, duration_mins, session_type FROM sleep_sessions ORDER BY start_time DESC LIMIT 10;"
```

If your `.env` uses different `POSTGRES_USER` or `POSTGRES_DB`, replace `lumi` in the commands above.

## Test AI Sleep Prediction

1. Open Swagger:

```text
http://localhost:8000/docs
```

2. Register a user:

```text
POST /api/v1/auth/register
```

Example body:

```json
{
  "email": "ai-test@example.com",
  "chronotype": "Neutral"
}
```

Copy the returned `user_id`.

3. Add at least 3 sleep sessions:

```text
POST /api/v1/sleep/manual
```

Set header:

```text
x-user-id: <user_id>
```

Example bodies:

```json
{
  "start_time": "2026-06-01T23:00:00+00:00",
  "end_time": "2026-06-02T06:30:00+00:00",
  "duration_mins": 450,
  "session_type": "nightly"
}
```

```json
{
  "start_time": "2026-06-02T23:30:00+00:00",
  "end_time": "2026-06-03T06:30:00+00:00",
  "duration_mins": 420,
  "session_type": "nightly"
}
```

```json
{
  "start_time": "2026-06-03T23:00:00+00:00",
  "end_time": "2026-06-04T05:30:00+00:00",
  "duration_mins": 390,
  "session_type": "nightly"
}
```

Wait a few seconds for `celery-storage` to persist the data.

4. Call prediction:

```text
GET /api/v1/predictions/sleep
```

Set header:

```text
x-user-id: <user_id>
```

Expected fields:

```json
{
  "predicted_duration_hours": 0,
  "confidence_interval_low": 0,
  "confidence_interval_high": 0,
  "probability_undersleep": 0,
  "snop_hours": 8,
  "based_on_nights": 3,
  "is_cached": false
}
```

Call the same endpoint again. The second response should have:

```json
"is_cached": true
```

## Chronos-2 Notes

Chronos-2 runs inside the `api` container, not as a separate service.

Check whether the model loads successfully:

```powershell
docker compose logs api --tail=200
```

Successful model load includes:

```text
Chronos-2 model loaded successfully
```

If Chronos fails to load or predict, the API still returns a fallback mean prediction.

## Reset Data

Stop containers but keep volumes:

```powershell
docker compose down
```

Stop containers and delete database/Redis volumes:

```powershell
docker compose down -v
```

Use `down -v` when you want a completely fresh database after schema changes.

Start again:

```powershell
docker compose up --build
```

## Common Issues

### `role "postgres" does not exist`

The default project user is usually `lumi`, not `postgres`. Use:

```powershell
docker compose exec -T timescaledb psql -U lumi -d lumi
```

### Sleep data does not appear immediately

`POST /api/v1/sleep/manual` dispatches a Celery task. Wait a few seconds, then check:

```powershell
docker compose logs celery-storage --tail=100
```

### Prediction returns cached data after adding sleep

The storage worker invalidates today's prediction cache after inserting sleep sessions. If stale cache remains, restart workers and API:

```powershell
docker compose restart api celery-storage celery-analytics
```

### Existing database does not match new code

`create_all()` creates missing tables for a fresh database, but it does not migrate existing tables. For local development, reset volumes:

```powershell
docker compose down -v
docker compose up --build
```
