/**
* Dennis Lin
* 109426873
* Homework #7
* CSE214 R05 
* Recitation TA: Vladimir Yevseenko
* Grading TA: Xi Zhang
 */

import java.util.LinkedList;
import big.data.DataSource;
import java.util.Collections;
import java.util.Comparator;

public class NeoDatabase extends LinkedList<NearEarthObject>{
    public static final String API_KEY = "bfGO2YE18RV5spNGHlcXXoeWjW2NYQdzfTqUzBa1";
    public static final String API_ROOT = "https://api.nasa.gov/neo/rest/v1/neo/browse?";
    
    /**
     * Brief:
     *  Builds a query URL given a page number. This should be a simple method which returns (API_ROOT + "page=" + pageNumber + "&api_key=" + API_KEY)
     *      Parameters:
     *          @param pageNumber - Integer ranging from 0 to 715 indicating the page the user wishes to load.
     *      Preconditions:
     *          0 ≤ page ≤ 715.
     *      Throws:
     *          IllegalArgumentException - If pageNumber is not in the valid range. 
     * @return API_ROOT + "page=" + pageNumber + "&api_key=" + API_KEY
     */
    public String buildQueryURL(int pageNumber) throws IllegalArgumentException{
        if(pageNumber < 0 || pageNumber > 715)
            throw new IllegalArgumentException();
        return API_ROOT + "page=" + pageNumber + "&api_key=" + API_KEY;
    }
    
    /**
     * Brief:
     *   Opens a connection to the data source indicated by queryURL and adds all NearEarthObjects found in the dataset.
     *       Parameters:
     *           @param queryURL - String containing the URL requesting a dataset from the NASA NeoW service (should be generated by buildQueryURL() above).
     *       Preconditions:
     *           queryURL is a non-null string representing a valid API request to the NASA NeoW service.
     *       Postconditions:
     *           All NearEarthObject records returned have been added to the database, or else a IllegalArgumentException has been thrown.
     *       Throws:
     *           @throws IllegalArgumentException
     *               If queryURL is null or cound not be resolved by the server.
     */
    public void addAll(String queryURL) throws IllegalArgumentException, NullPointerException{
        if(queryURL == null)
            throw new IllegalArgumentException("queryURL is null");
        DataSource ds = DataSource.connectJSON(queryURL);
        ds.load();
        if(ds.load() == null){
            throw new NullPointerException();
        }else{    
        NearEarthObject[] myDatas = ds.fetchArray("NearEarthObject",
                "near_earth_objects/neo_reference_id", 
                "near_earth_objects/name",
                "near_earth_objects/absolute_magnitude_h",
                "near_earth_objects/estimated_diameter/kilometers/estimated_diameter_min",
                "near_earth_objects/estimated_diameter/kilometers/estimated_diameter_max",
                "near_earth_objects/is_potentially_hazardous_asteroid",
                "near_earth_objects/close_approach_data/epoch_date_close_approach",
                "near_earth_objects/close_approach_data/miss_distance/kilometers",
                "near_earth_objects/close_approach_data/orbiting_body"
                );
        for(int i = 0; i < myDatas.length; i++)
            super.add(myDatas[i]);   
        }
    }
    
    /**
      *Brief:
      *  Sorts the database using the specified Comparator of NearEarthObjects.
      *      Parameters:
      *          @param comp - Comparator of NearEarthObjects which will be used to sort the database. This parameter can be any of the required Comparator classes listed above.
      *      Preconditions:
      *          comp is not null.
      *      Postconditions:
      *          The database has been sorted based on the order specified by the inidcated Comparator of NearEarthObjects.
      *      Throws:
      *          IllegalArgumentException
      *              If comp is null.
     */
    public void aSort(Comparator<NearEarthObject> comp) throws IllegalArgumentException{
        if(comp == null)
            throw new IllegalArgumentException("comp is null");
        Collections.sort(this, comp);
    }
    
    /**
     * Brief:
     *   Displays the database in a neat, tabular form, listing all member variables for each NearEarthObject. Note the table should be printed in the order specified by the last sort() call.
     *      Preconditions:
     *          This NeoDatabase is initialized and not null.
     *      Postconditions:
     *          The table has been printed to the console but remains unchanged.
     */
    public void printTable(){
        System.out.println("     ID   |      Name    | Mag. | Diameter | Danger | Close Date | Miss Dist | Orbits"
                         + "\n======================================================================================");
        for(int i = 0; i < this.toArray().length; i++)
            System.out.println("  " + this.toArray()[i]);
    }
}
