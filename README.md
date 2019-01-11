<img src="https://raw.githubusercontent.com/akvo/akvo-web/develop/code/wp-content/themes/Akvo-responsive/images/flow60px.png" />

This is a PoC for SSO with Auth0

Instructions to run it:

1. Build the modified KeyCloak client libraries:
    1. Checkout https://github.com/akvo/keycloak/tree/poc/auth0-sso
    1. Switch to the poc/auth0-sso branch
    1. cd /adapters/oidc
    1. mvn install
1. In Flow:
    1. Change secret in `GAE/war/WEB-INF/auth0.json` to the one in [Auth0](https://manage.auth0.com/#/applications/DhFdUkPQPwosdWatLsTYO3e85Tn9z7RE/settings)      
    1. Start Flow: `docker-compose up --build -d`
    1. Add the user that you will login with: `wget 'http://localhost:8888/webapp/testharness?action=setupTestUser&email=foo@bar.com'`

You should be able to login with Auth0 now.

To run Lumen:

1. Checkout branch `poc/sso-auth0`
2. Run: `docker-compose up --build -d`

You should be able to login with Auth0 now.

## Gotcha's

1. You will probably need to ask Dan for the Auth0 credentials to see the secret.
1. Sometimes, the redirect from Flow to Auth0 includes a jsessionid in the callback url. This will cause Auth0 to complain about `Callback URL mismatch.`. In this case, just visit `http://localhost:8888/sso/login?scope=email` directly to force the Auth0 login. 
1. There is no single logout implemented. To simulate it, clean the cookies of your local Flow and Lumen, and of dantestakvo.eu.auth0.com