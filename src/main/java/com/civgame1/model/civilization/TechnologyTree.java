package com.civgame1.model.civilization;

import java.util.*;

public class TechnologyTree {

    private final Map<String, Technology> allTechnologies;
    private final Set<Technology> researchedTechs;

    public TechnologyTree() {
        this.allTechnologies = new HashMap<>();
        this.researchedTechs = new HashSet<>();
        initializeTechnologies();
    }

    private void initializeTechnologies() {
        Technology storage1 = new Technology("Storage Upgrade 1", 50);
        Technology storage2 = new Technology("Storage Upgrade 2", 100, storage1);
        
        Technology stoneMine = new Technology("Stone Mine Tech", 60);
        Technology ironMine = new Technology("Iron Mine Tech", 120, stoneMine);
        Technology advancedTools = new Technology("Advanced Tools", 150, ironMine);
        
        Technology townTech = new Technology("Town Tech", 200);

        allTechnologies.put("Storage Upgrade 1", storage1);
        allTechnologies.put("Storage Upgrade 2", storage2);
        allTechnologies.put("Stone Mine Tech", stoneMine);
        allTechnologies.put("Iron Mine Tech", ironMine);
        allTechnologies.put("Advanced Tools", advancedTools);
        allTechnologies.put("Town Tech", townTech);
    }

    public boolean isUnlocked(String techName) {
        Technology tech = allTechnologies.get(techName);
        return tech != null && researchedTechs.contains(tech);
    }
    
    public Technology getTechnology(String techName) {
        return allTechnologies.get(techName);
    }
    
    public void unlockTechnology(String techName) {
        Technology tech = allTechnologies.get(techName);
        if (tech != null) {
            tech.complete();
            researchedTechs.add(tech);
        }
    }

    public List<Technology> getAvailableTechnologies() {
        List<Technology> available = new ArrayList<>();
        for (Technology tech : allTechnologies.values()) {
            if (!researchedTechs.contains(tech) && tech.canResearch()) {
                available.add(tech);
            }
        }
        return available;
    }
    
    public Set<Technology> getResearchedTechs() {
        return new HashSet<>(researchedTechs);
    }
}
