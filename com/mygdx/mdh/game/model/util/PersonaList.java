package com.mygdx.mdh.game.model.util;

import java.util.*;

import com.mygdx.mdh.game.model.Character;

/**
 * Created by zubisoft on 25/09/2016.
 */
public class PersonaList {


    HashMap<String,List<Character>> personaMap;

    public PersonaList (List<Character> characters) {
        personaMap = new HashMap<>();

        for (Character c: characters) {
            if (personaMap.containsKey(c.getPersona())) {
                personaMap.get(c.getPersona()).add(c);
            } else {
                personaMap.put(c.getPersona(),new ArrayList<>());
                personaMap.get(c.getPersona()).add(c);
            }
        }

    }

    public Character get(String persona, int i) {
        return personaMap.get(persona).get(i);
    }

    public List<Character> get(String persona) {
        return personaMap.get(persona);
    }



    public Set<String> getPersonas() {
        return personaMap.keySet();
    }

}
