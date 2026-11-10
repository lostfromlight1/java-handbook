package com.handbook.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class JpaH2Demo {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("handbookPU");
        try {
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                User user = new User("Lin");
                em.persist(user);
                em.getTransaction().commit();

                User loaded = em.find(User.class, user.getId());
                System.out.println("name=" + loaded.getName());
                System.out.println("idNotNull=" + (loaded.getId() != null));
            } finally {
                em.close();
            }
        } finally {
            emf.close();
        }
        System.out.println("jpaH2=ok");
    }
}
