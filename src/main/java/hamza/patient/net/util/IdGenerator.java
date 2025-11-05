package hamza.patient.net.util;

import java.util.UUID;

/**
 * Tâche 2 : Classe utilitaire pour générer des identifiants uniques.
 */
public class IdGenerator {

    /**
     * Constructeur privé car c'est une classe 100% statique.
     * On ne veut pas que quelqu'un puisse créer un objet 'new IdGenerator()'.
     */
    private IdGenerator() {
    }

    /**
     * Génère un nouvel identifiant unique basé sur UUID.
     * (Requis par Tâche 2)
     *
     * @return Une chaîne de caractères représentant un ID unique.
     */
    public static String newId() {
        // UUID.randomUUID() est la méthode standard en Java
        // pour garantir l'unicité (Critère d'acceptation Tâche 2).
        return UUID.randomUUID().toString();
    }
}