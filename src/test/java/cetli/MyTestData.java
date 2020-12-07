package cetli;

import static cetli.Database.database;

public class MyTestData {
    
    private static final String[][] connections = {
        {"Planes of Existence", "Astral Plane"},
        {"Planes of Existence", "Ethereal Plane"},
        {"Astral Plane", "Astral Projection"},
        {"Astral Plane", "Probability Travel"},
        {"Ethereal Plane", "Etherealness"},
        
        {"Devotions", "Telepathy"},
        {"Devotions", "Object Reading"},
        {"Devotions", "Sensitivity to Psychic Impressions"},
        {"Devotions", "Detection of Evil/Good"},
        {"Devotions", "Detection of Magic"},
        {"Devotions", "ESP"},
        {"Devotions", "Hypnosis"},
        {"Devotions", "Molecular Agitation"},
        {"Devotions", "Empathy"},
        {"Devotions", "Reduction"},
        {"Devotions", "Cell Adjustment"},
        {"Devotions", "Expansion"},
        {"Devotions", "Animal Telepathy"},
        {"Devotions", "Levitation"},
        {"Devotions", "Domination"},
        {"Devotions", "Mind over Body"},
        {"Devotions", "Invisibility"},
        {"Devotions", "Precognition"},
        {"Devotions", "Suspend Animation"},
        {"Devotions", "Body Equilibrium"},
        {"Devotions", "Clairaudience"},
        {"Devotions", "Clairvoyance"},
        {"Devotions", "Body Weaponry"},
        
        {"Sciences", "Telekinesis"},
        {"Sciences", "Dimension Walking"},
        {"Sciences", "Astral Projection"},
        {"Sciences", "Molecular Rearrangement"},
        {"Sciences", "Molecular Manipulation"},
        {"Sciences", "Body Control"},
        {"Sciences", "Mind Bar"},
        {"Sciences", "Telepathic Projection"},
        {"Sciences", "Dimension Door"},
        {"Sciences", "Teleportation"},
        {"Sciences", "Etherealness"},
        {"Sciences", "Shape Alteration"},
        {"Sciences", "Aura Alteration"},
        {"Sciences", "Telempathic Projection"},
        {"Sciences", "Mass Domination"},
        {"Sciences", "Probability Travel"},
        {"Sciences", "Energy Control"},
        
        {"Psionic Abilities", "Telekinesis"},
        {"Psionic Abilities", "Dimension Walking"},
        {"Psionic Abilities", "Astral Projection"},
        {"Psionic Abilities", "Molecular Rearrangement"},
        {"Psionic Abilities", "Molecular Manipulation"},
        {"Psionic Abilities", "Body Control"},
        {"Psionic Abilities", "Mind Bar"},
        {"Psionic Abilities", "Detection of Evil/Good"},
        {"Psionic Abilities", "Detection of Magic"},
        {"Psionic Abilities", "ESP"},
        {"Psionic Abilities", "Hypnosis"},
        {"Psionic Abilities", "Molecular Agitation"},
        {"Psionic Abilities", "Telepathic Projection"},
        {"Psionic Abilities", "Dimension Door"},
        {"Psionic Abilities", "Teleportation"},
        {"Psionic Abilities", "Etherealness"},
        {"Psionic Abilities", "Shape Alteration"},
        {"Psionic Abilities", "Empathy"},
        {"Psionic Abilities", "Cell Adjustment"},
        {"Psionic Abilities", "Animal Telepathy"},
        {"Psionic Abilities", "Aura Alteration"},
        {"Psionic Abilities", "Telempathic Projection"},
        {"Psionic Abilities", "Mass Domination"},
        {"Psionic Abilities", "Probability Travel"},
        {"Psionic Abilities", "Telepathy"},
        {"Psionic Abilities", "Object Reading"},
        {"Psionic Abilities", "Sensitivity to Psychic Impressions"},
        {"Psionic Abilities", "Reduction"},
        {"Psionic Abilities", "Expansion"},
        {"Psionic Abilities", "Levitation"},
        {"Psionic Abilities", "Domination"},
        {"Psionic Abilities", "Mind over Body"},
        {"Psionic Abilities", "Invisibility"},
        {"Psionic Abilities", "Precognition"},
        {"Psionic Abilities", "Suspend Animation"},
        {"Psionic Abilities", "Body Equilibrium"},
        {"Psionic Abilities", "Clairaudience"},
        {"Psionic Abilities", "Clairvoyance"},
        {"Psionic Abilities", "Body Weaponry"},
        {"Psionic Abilities", "Energy Control"}
    };
    
    private static final String[] articles = {
        "Psionic Abilities",
        
        "Devotions",
        "Sciences",
        
        "Planes of Existence",
        "Astral Plane",
        "Ethereal plane",
        
        "Psionic Effects",
        
        
        "Telekinesis", 
        "Dimension Walking",
        "Astral Projection",
        "Molecular Rearrangement",
        "Molecular Manipulation",
        "Body Control",
        "Mind Bar",
        "Detection of Evil/Good",
        "Detection of Magic",
        "ESP",
        "Hypnosis",
        "Molecular Agitation",
        "Telepathic Projection",
        "Dimension Door",
        "Teleportation",
        "Etherealness",
        "Shape Alteration",
        "Empathy",
        "Cell Adjustment",
        "Animal Telepathy",
        "Aura Alteration",
        "Telempathic Projection",
        "Mass Domination",
        "Probability Travel",
        "Telepathy",
        "Object Reading",
        "Sensitivity to Psychic Impressions",
        "Reduction",
        "Expansion",
        "Levitation",
        "Domination",
        "Mind over Body",
        "Invisibility",
        "Precognition",
        "Suspend Animation",
        "Body Equilibrium",
        "Clairaudience",
        "Clairvoyance",
        "Body Weaponry",
        "Energy Control"
    };
    
    public static void load() {
        for (String s: articles) {
            database.newArticle(s);
        }
        
        for (String[] sa: connections) {
            String p = sa[0];
            String c = sa[1];
            database.addRelationship(database.search(p).get(0), database.search(c).get(0));
        }
    }
    public static void main(String[] args) throws Exception {
        Database.database = new Database("test/mytestdatabase");
        database.clearTables();
        database.createTables();
        load();
    }
}
