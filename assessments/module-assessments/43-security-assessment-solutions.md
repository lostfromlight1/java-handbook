# Solutions: Module 43 — Security

1. AuthN proves identity; AuthZ decides permitted action on resource.
2. Parameterized queries / `PreparedStatement`; never concat user input into SQL.
3. Compare resource owner to authenticated principal on every access.
4. Logs are copied/stored widely; tokens become bearer credentials if leaked.
5. PBKDF2 is slow with salt; SHA-256 alone is fast and unsuitable for passwords.
6. Confidentiality (encryption) and server authenticity (certificate validation).
7. Secret manager / vault / K8s secrets with rotation—not git.
8. `delete(...).with(user("alice")).andExpect(status().isForbidden())` when not owner.
9. SAST analyzes code patterns; dependency scan matches known CVEs in libraries.
10. Allow-list destinations; block arbitrary user-supplied URLs in server-side fetches.

11. `input-guard=ok`
12. Separates SQL structure from data; driver binds values safely.
