package com.example.smartlib_catalog.controller;


import com.example.smartlib_catalog.entity.Livre;
import com.example.smartlib_catalog.service.LivreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/v1/livres")
@Tag(name = "Livres", description = "API for managing books")
public class LivreController {


    @Autowired
    LivreService livreService;

    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }

    @Operation(summary = "Add a new book", description = "This API endpoint allows you to add a new book to the system")
    @ApiResponse(responseCode = "201", description = "Book created successfully")
    @PostMapping


    public ResponseEntity<Livre> save(@RequestBody Livre livre){
            Livre savedLivre = livreService.save(livre);
            return new ResponseEntity<>(savedLivre, HttpStatus.CREATED);
    }


    @Operation(summary = "Get all books", description = "This API endpoint allows you to retrieve all books")
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully")

    @GetMapping

    public ResponseEntity<List<Livre>> getAll(){

        List<Livre> livres = livreService.getAll();
        return new ResponseEntity<>(livres, HttpStatus.OK);

    }


    @Operation(summary = "Get a book by ID", description = "This API endpoint allows you to fetch a book's details using its ID")
    @ApiResponse(responseCode = "200", description = "Book found")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @GetMapping("/{id}")

    public ResponseEntity<Livre> getByid(@PathVariable Long id){

        Livre livre = livreService.getByid(id);

        return livre != null ? new ResponseEntity<>(livre, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Operation(summary = "Update an existing book", description = "This API endpoint allows you to update a book's details")
    @ApiResponse(responseCode = "200", description = "Book updated successfully")
    @PutMapping("/{id}")

    public ResponseEntity<Livre> update(@PathVariable Long id, @RequestBody Livre livre){
        Livre updatelivre = livreService.update(id, livre);
        return new ResponseEntity<>(updatelivre, HttpStatus.OK);
    }

    @Operation(summary = "Delete a book", description = "This API endpoint allows you to delete a book by its ID")
    @ApiResponse(responseCode = "204", description = "Book deleted successfully")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        livreService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Search books by title", description = "This API endpoint allows you to search books by their title")
    @ApiResponse(responseCode = "200", description = "Books found")
    @GetMapping("/search")

    public ResponseEntity<List<Livre>> searchByTitle(@RequestParam String title) {
        List<Livre> livres = livreService.searchByTitle(title);

        return new ResponseEntity<>(livres, HttpStatus.OK);

    }

    @Operation(summary = "Filter books by criteria", description = "This API endpoint allows you to filter books by author, title, or genre")
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    @GetMapping("/filter")

    public ResponseEntity<List<Livre>> filterByGenreAndAuthor(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String author) {
        List<Livre> livres = livreService.filterByGenreAndAuthor(genre, author);
        return new ResponseEntity<>(livres, HttpStatus.OK);

    }

}
