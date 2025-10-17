# ZIO HTTP App Composition CORS Bug Reproduction

This repository demonstrates a bug in ZIO HTTP where combining multiple HTTP apps with different CORS configurations causes the last app's CORS configuration to override previous ones.

## Environment

- Scala: 3.3.6
- ZIO: 2.1.14
- ZIO HTTP: 3.0.1

## Problem Description

When composing two ZIO HTTP apps with different CORS middleware configurations using the `++` operator, the CORS configuration from the second app appears to override the CORS configuration from the first app, affecting both routes.

### Expected Behavior

- `/app1/health` should accept requests from `http://localhost:5173` only
- `/app2/health` should accept requests from any origin (`*`)

### Actual Behavior

Both `/app1/health` and `/app2/health` accept requests from any origin (`*`), as if both routes are using the CORS configuration from `app2`.

The CORS middleware is applied to each app separately before composition:
```scala
val app1 = Routes(...) @@ Cors.middleware(strictConfig)
val app2 = Routes(...) @@ Cors.middleware(permissiveConfig)
val combinedApp = app1 ++ app2
```

Despite this, the CORS behavior suggests that the second middleware configuration is somehow affecting the first app's routes.

## Reproduction Steps

1. Clone this repository
2. Run the application:
   ```bash
   sbt run
   ```
3. Test the endpoints with different origins:
   ```bash
   # Test app1 with the allowed origin - should work
   curl -H "Origin: http://localhost:5173" http://localhost:8080/app1/health -v

   # Test app1 with a different origin - should be blocked, but isn't
   curl -H "Origin: http://localhost:3000" http://localhost:8080/app1/health -v

   # Test app2 with any origin - should work (and does)
   curl -H "Origin: http://localhost:3000" http://localhost:8080/app1/health -v
   ```
