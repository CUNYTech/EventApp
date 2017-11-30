package com.example.natia.flock1;

import java.util.ArrayList;

/**
 * Created by napti on 11/29/2017.
 */

public class Events {

    //public static ArrayList<Events> getEventsFromFB(DataSnapshot dataSnapshot, Context context) {
        final ArrayList<Events> eventList = new ArrayList<>();
    }
        //try {
            // Load data
            //String jsonString = loadJsonFromAsset("recipes.json", context);
            //JSONObject json = new JSONObject(jsonString);
            //JSONArray recipes = json.getJSONArray("recipes");


            // Get Recipe objects from data
            /**
            for(int i = 0; i < recipes.length(); i++){
                Recipe recipe = new Recipe();

                recipe.title = recipes.getJSONObject(i).getString("title");
                recipe.description = recipes.getJSONObject(i).getString("description");
                recipe.imageUrl = recipes.getJSONObject(i).getString("image");
                recipe.instructionUrl = recipes.getJSONObject(i).getString("url");
                recipe.label = recipes.getJSONObject(i).getString("dietLabel");

                recipeList.add(recipe);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return eventList;
    }

    private static String loadJsonFromAsset(String filename, Context context) {
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }*/


