/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cit260.piggysRevenge.view;

import cit260.piggysRevenge.control.MapControl;
import cit260.piggysRevenge.exceptions.MapControlException;
import java.awt.Point;
import static java.lang.Math.abs;
import piggysrevenge.PiggysRevenge;

/**
 *
 * @author natebolton
 */
class DistanceView extends View {

    public DistanceView() {
        
    }

    @Override
    public void display() {
        //Get player location
        Point playerCoords = PiggysRevenge.getCurrentGame().getPlayer().getCoordinates();
        //Get wolf location
        Point wolfCoords = PiggysRevenge.getCurrentGame().getWolf().getCoordinates();
        //Get list of actor coordinates (as Points) in a list from MapControl
        Point[] actorPoints = MapControl.getActorPoints(PiggysRevenge.getCurrentGame().getMap());
        String[] actorNames = MapControl.getActorNames(PiggysRevenge.getCurrentGame().getMap());
        //make new lists to add the wolf.
        Point[] allPoints = new Point[actorPoints.length+1];
        int index = 0;
        for (Point point : actorPoints) {
            allPoints[index] = point;
            index++;
        }
        allPoints[index] = wolfCoords;
        String[] allNames = new String[actorNames.length+1];
        index = 0;
        for (String name : actorNames) {
            allNames[index] = name;
            index++;
        }
        //variable wolfName needed later
        allNames[index] = PiggysRevenge.getCurrentGame().getWolf().getName() + " the Wolf";

//        this.console.println("allPoints is:  ");
//        this.console.println(Arrays.toString(allPoints));
//        this.console.println("Player's coords is:  ");
//        this.console.println(playerCoords.toString());
        
        //convert list of points to list of distances from player
        double[] distances = new double[allPoints.length];
        index = 0;
        for (Point point : allPoints) {
            try {
                double result = MapControl.calcDistance(playerCoords.x, playerCoords.y, point.x, point.y);
               
                    result *= 10.0;
                    result = Math.round(result);
                    result /= 10.0;
                    distances[index] = abs(result);
                       
                index++;
            } catch (MapControlException ex) {
                this.console.println(ex.getMessage());
            }
        }
        //sort lists
        //this.console.println("distances unsorted:  ");
        //this.console.println(Arrays.toString(distances));
        //bubble sort all three arrays (distance, points, names)
        if (PiggysRevenge.getCurrentGame().getPlayer().getCurrentShoes() != null) {            
            if ("Alien Boots".equals(PiggysRevenge.getCurrentGame().getPlayer().getCurrentShoes().getName())) {
                for (int n = 0; n < distances.length+1; n++) {
                    for (int m = 0; m < 5 - n; m++) {
                        if (distances[m] > distances[m + 1]) {
                            Double swapDouble = distances[m];
                            distances[m] = distances[m + 1];
                            distances[m + 1] = swapDouble;
                            Point swapPoint = allPoints[m];
                            allPoints[m] = allPoints[m + 1];
                            allPoints[m + 1] = swapPoint;
                            String swapString = allNames[m];
                            allNames[m] = allNames[m + 1];
                            allNames[m + 1] = swapString;
                        }
                    }
                }
                this.console.println("WEARING ALIEN BOOTS - DISTANCES ARE SORTED");
            }
        }
        //this.console.println("distances sorted:  ");
        //this.console.println(Arrays.toString(distances));
        //display sorted List of actors and wolf
        this.console.println("DISTANCES:\n");
        StringBuilder line = new StringBuilder("                                         ");
        line.insert(4, "ACTOR");
        line.insert(25, "DISTANCE");
        this.console.println(line.toString());
        line = new StringBuilder("                                         ");
        line.insert(4, "-----");
        line.insert(25, "--------");
        this.console.println(line.toString());
        index = 0;
        for (String name : allNames) {
            line = new StringBuilder("                                         ");
            //Actor value = MapControl.getActorFromPoint(point,PiggysRevenge.getCurrentGame().getMap());
            if (name == null) {
                this.console.println("ERROR:  null value");
                return;
//                line.insert(0, Integer.toString(index+1) + ":  Wolf");
            } else {
                line.insert(0, Integer.toString(index+1) + ":  " + name);
            }
            switch (name) {
                case "Piggy1":
                case "Piggy2":
                case "Piggy3":
                case "Piggy4":
                    if (PiggysRevenge.getCurrentGame().getPlayer().getCurrentHat() != null) {
                        if ("Fedora".equals(PiggysRevenge.getCurrentGame().getPlayer().getCurrentHat().getName())) {
                            line.insert(25, distances[index]);
                        } else {
                            line.insert(25, "???");
                        }
                    } else {
                        line.insert(25, "???");
                    }
                    break;
                case "Builder":
                    if (PiggysRevenge.getCurrentGame().getPlayer().getCurrentHat() != null) {
                        if ("Hard Hat".equals(PiggysRevenge.getCurrentGame().getPlayer().getCurrentHat().getName())) {
                            line.insert(25, distances[index]);
                        } else {
                            line.insert(25, "???");
                        }
                    } else {
                        line.insert(25, "???");
                    }
                    break;
                default:
                    if (PiggysRevenge.getCurrentGame().getPlayer().getCurrentHat() != null) {
                        if ("Cowboy Hat".equals(PiggysRevenge.getCurrentGame().getPlayer().getCurrentHat().getName())) {
                            line.insert(25, distances[index]);
                        } else {
                            line.insert(25, "???");
                        }
                    } else {
                        line.insert(25, "???");
                    }
            }
            //line.insert(25, distances[index]);
            this.console.println(line.toString());
            index++;
        }

    }
    
    @Override
    public boolean doAction(String input1) {
        return false;  
    }
    
}
