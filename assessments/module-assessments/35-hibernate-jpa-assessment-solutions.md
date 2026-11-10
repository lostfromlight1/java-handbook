# Solutions: Module 35

1. JPA is API/spec; Hibernate is implementation (others: EclipseLink).
2. `persist` inserts new managed entity; `merge` reattaches detached state (may copy).
3. Database generates id on insert (auto-increment/identity column).
4. Access lazy association outside active persistence context/transaction.
5. Many side holds FK (`@JoinColumn`); one side often `mappedBy`.
6. Eagerly load association in one query to avoid N+1.
7. RESOURCE_LOCAL uses EntityTransaction/JDBC; JTA uses container transaction manager.
8. Single persistence context / EntityManager session.
9. `User` is reserved/problematic identifier in H2 SQL; maps to `app_user` table.
10. Holds DB session open across view/rendering or HTTP response building — leaks pattern into REST layer.

11. `name=Lin`
12. `Second-level cache disabled` (HHH000026).
