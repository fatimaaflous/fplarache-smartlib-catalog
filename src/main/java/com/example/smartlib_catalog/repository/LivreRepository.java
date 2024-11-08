package com.example.smartlib_catalog.repository;


import com.example.smartlib_catalog.entity.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {

    List<Livre> findByTitreContainingIgnoreCase(String titre); // chercher le livre par titre

    List<Livre> findByGenreOrAuteur(String genre, String auteur); //filtrer soit par auteur ou genre du livre
}
