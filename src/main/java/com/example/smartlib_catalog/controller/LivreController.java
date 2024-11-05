package com.example.smartlib_catalog.controller;


import com.example.smartlib_catalog.entity.Livre;
import com.example.smartlib_catalog.service.LivreService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.servers.Server;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/livres")


@OpenAPIDefinition(
        info = @Info(
                title = "Gestion du catalog des livre",
                description = " Gerer les opération de catalogue",
                version = "1.0.0"
        ),

        servers = @Server(
                url = "http://localhost:8081/"
        )
)
public class LivreController {


    @Autowired
    LivreService livreService;


    @PostMapping

    @Operation(
            summary = "Ajouter Un compte",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "Application/json",
                            schema = @Schema(implementation = Livre.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "ajouter par succéses",
                            content = @Content(
                                    mediaType = "Application/json",
                                    schema = @Schema(implementation = Livre.class))
                    ),

                    @ApiResponse(responseCode = "400",description = "erreur données"),
                    @ApiResponse(responseCode ="500", description = "erreur server")
            }
    )
    public ResponseEntity<Livre> save(@RequestBody Livre livre){

        return ResponseEntity.ok(livreService.save(livre));
    }


    @GetMapping
    @Operation(
            summary = "Récupérer la liste de tous les livres",
            description = "Récupère tous les livres dans le catalogue"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livre.class))),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    public ResponseEntity<List<Livre>> getAll(){

        List<Livre> livreList = livreService.getAll();
        return ResponseEntity.ok(livreList);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Récupérer un livre par ID",
            description = "Récupère les informations d'un livre spécifique par son ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livre trouvé",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livre.class))),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    })
    public ResponseEntity<Livre> getByid(@PathVariable Long id){

        return ResponseEntity.ok(livreService.getByid(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Mettre à jour un livre par ID",
            description = "Met à jour les informations d'un livre existant"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livre mis à jour",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livre.class))),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    })
    public ResponseEntity<Livre> update(@PathVariable Long id, @RequestBody Livre livre){
        Livre updatelivre = livreService.update(id, livre);
        return ResponseEntity.ok(updatelivre);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer un livre par ID",
            description = "Supprime un livre spécifique par son ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livre supprimé"),
            @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    })
    public void delete(@PathVariable Long id){
        livreService.delete(id);
    }


    @GetMapping("/search")
    @Operation(
            summary = "Rechercher des livres par titre",
            description = "Recherche des livres dans le catalogue par titre"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livre.class))),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    public ResponseEntity<List<Livre>> searchByTitle(@RequestParam String title) {
        List<Livre> livres = livreService.searchByTitle(title);
        return ResponseEntity.ok(livres);
    }

    @GetMapping("/filter")
    @Operation(
            summary = "Filtrer les livres par genre et auteur",
            description = "Filtre les livres du catalogue par genre et/ou auteur"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livre.class))),
            @ApiResponse(responseCode = "500", description = "Erreur serveur")
    })
    public ResponseEntity<List<Livre>> filterByGenreAndAuthor(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String author) {
        List<Livre> livres = livreService.filterByGenreAndAuthor(genre, author);
        return ResponseEntity.ok(livres);
    }

}
