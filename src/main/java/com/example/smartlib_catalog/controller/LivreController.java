package com.example.smartlib_catalog.controller;


import com.example.smartlib_catalog.entity.Livre;
import com.example.smartlib_catalog.service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/livres")
public class LivreController {


    @Autowired
    LivreService livreService;


    @PostMapping
    public ResponseEntity<Livre> save(@RequestBody Livre livre){

        return ResponseEntity.ok(livreService.save(livre));
    }


    @GetMapping
    public ResponseEntity<List<Livre>> getAll(){

        List<Livre> livreList = livreService.getAll();
        return ResponseEntity.ok(livreList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livre> getByid(@PathVariable Long id){

        return ResponseEntity.ok(livreService.getByid(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livre> update(@PathVariable Long id, @RequestBody Livre livre){
        Livre updatelivre = livreService.update(id, livre);
        return ResponseEntity.ok(updatelivre);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        livreService.delete(id);
    }


    @GetMapping("/search")
    public ResponseEntity<List<Livre>> searchByTitle(@RequestParam String title) {
        List<Livre> livres = livreService.searchByTitle(title);
        return ResponseEntity.ok(livres);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Livre>> filterByGenreAndAuthor(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String author) {
        List<Livre> livres = livreService.filterByGenreAndAuthor(genre, author);
        return ResponseEntity.ok(livres);
    }

}
