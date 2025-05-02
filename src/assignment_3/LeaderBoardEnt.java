/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment_3;

/**
 *
 * @author Ayxan
 */

public class LeaderBoardEnt {
    private final String name;
    private final int collectedBaskets;

    public LeaderBoardEnt(String name, int collectedBaskets) {
        this.name = name;
        this.collectedBaskets = collectedBaskets;
    }

    @Override
    public String toString() {
        return name + " - Baskets: " + collectedBaskets;
    }
}
