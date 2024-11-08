package com.example.smartlib_catalog.service;


import com.example.smartlib_catalog.entity.Livre;
import com.example.smartlib_catalog.repository.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivreService {

    @Autowired
    LivreRepository livreRepository;

    public Livre save(Livre livre){
        return livreRepository.save(livre);
    }


    public List<Livre> getAll(){
        return livreRepository.findAll();
    }

    public Livre getByid(Long id){
        return livreRepository.getById(id);
    }

    public void delete(Long id){
        livreRepository.deleteById(id);
    }


    public Livre update( Long id, Livre livre){
        return livreRepository.findById(id).map( livre1 -> {
            livre1.setTitre(livre.getTitre());
            livre1.setAuteur(livre.getAuteur());
            livre1.setDescription(livre.getDescription());
            livre1.setGenre(livre.getGenre());
            livre1.setIsbn(livre.getIsbn());
            livre1.setDatePublication(livre.getDatePublication());
            return livreRepository.save(livre1);
            }).orElseThrow(() -> new RuntimeException("Livre nest pas trouve !!!"));

    }


    public List<Livre> searchByTitle(String title) {
        return livreRepository.findByTitreContainingIgnoreCase(title);
    }

    public List<Livre> filterByGenreAndAuthor(String genre, String author) {
        return livreRepository.findByGenreOrAuteur(genre, author);
    }

}
