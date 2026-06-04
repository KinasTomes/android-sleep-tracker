# Backend Tests

Install test dependencies:

```powershell
python -m pip install -r requirements-dev.txt
```

Run all tests from the `backend` folder:

```powershell
python -m pytest -q
```

Run a single test file:

```powershell
python -m pytest tests/test_survey.py -q
```

Some tests encode expected behavior from `docs/` and `api_description.pdf`.
If they fail, treat the failure as a backend issue to fix rather than changing
the test to match the current bug.
