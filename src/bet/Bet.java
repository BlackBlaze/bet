package bet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Miguel
 */
public class Bet {

    final static int attempts = 1000;
    
    final static int numPlayers = 100;
    final static int numBoxesCanOpen = 50;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Bet bet = new Bet();
        bet.start();

    }

    public void start() {
        doGame(new RandomStrategy());
        doGame(new SmartStrategy());
    }

    private void doGame(Strategy strategy) {
        int win = 0;
        int lost = 0;

        for (int j = 0; j < attempts; j++) {

            //Preparación
            boolean gameLost = false;
            List<Integer> boxes = new ArrayList<>();
            for (int i = 0; i < numPlayers; i++) {
                boxes.add(i);
            }
            Collections.shuffle(boxes);

            //Empieza el juego
            for (int i = 0; i < numPlayers && !gameLost; i++) {
                boolean foundIt = strategy.doStrategy(i, boxes);
                if (!foundIt) {
                    //Perdieron
                    lost++;
                    gameLost = true;
                }
            }
            if (!gameLost) {
                //Ganaron
                win++;
            }
        }

        Double winPer = (double) win / attempts * 100;
        Double lostPer = (double) lost / attempts * 100;
        System.out.println("Victorias: " + win + "\t Porcentage: " + winPer + "%");
        System.out.println("Derrotas: " + lost + "\t Porcentage: " + lostPer + "%");
    }

    public interface Strategy {

        public boolean doStrategy(Integer bill, List<Integer> list);
    }

    public class RandomStrategy implements Strategy {

        @Override
        public boolean doStrategy(Integer bill, List<Integer> list) {
            List<Integer> copyList = new ArrayList(list);
            Random rand = new Random();
            int i = 0;
            while (copyList.size() > 0 && i < numBoxesCanOpen) {
                int index = rand.nextInt(copyList.size());
                Integer selected = copyList.remove(index);
                if (selected.equals(bill)) {
                    return true;
                }
                i++;
            }
            return false;
        }

    }

    public class SmartStrategy implements Strategy {

        @Override
        public boolean doStrategy(Integer bill, List<Integer> list) {
            Integer selected = list.get(bill);
            boolean foundIt = selected.equals(bill);
            for (int i = 0; i < numBoxesCanOpen && !foundIt; i++) {
                selected = list.get(selected);
                foundIt = selected.equals(bill);
            }
            return foundIt;
        }
    }
}
