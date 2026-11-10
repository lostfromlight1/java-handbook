# Solutions: Module 39 — Authentication and Authorization

1. AuthN proves identity; AuthZ decides permitted actions on resources.
2. Insecure direct object reference—access resource by id without ownership check; enforce principal owns resource or has role.
3. JavaScript cannot read cookie—reduces XSS token theft (not XSS itself).
4. No—payload is base64-encoded, readable; signature provides integrity.
5. Detect stolen refresh token reuse; revoke token family.
6. Authorization server issues tokens; resource server validates and serves API.
7. Public clients (SPAs/mobile) without embedded client secret.
8. Often CSRF disabled for stateless bearer APIs; still required for cookie-session browser forms.
9. URL rules coarse path/method; method security fine-grained on service methods (defense in depth).
10. Deny unless explicitly permitted (authenticated routes need rules).
