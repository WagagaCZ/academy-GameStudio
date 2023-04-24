package sk.tuke.gamestudio.client.game.poker.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Logic {
    private List<Integer> duplicates;
    private List<Card> hand;
    public boolean isFlush(){
        Card.Type type = hand.get(0).getType();
        for(Card card: hand){
            if (card.getType() != type){
                return false;
            }
        }
        return true;
    }
    public boolean isStraight(){
        List<Integer> numbers = new ArrayList<>();
        for(Card card: hand){
            numbers.add(card.getValue());
        }
        numbers = numbers.stream().sorted().toList();
        for (int i = 0; i < 5; i++) {
            if (numbers.get(i) - i != numbers.get(0)){
                return false;
            }
        }
        return true;
    }

    public int calculateScore() {
        int score = 0;
        if (isFlush() && isStraight()){
            score = 2000;
        } else if (isFlush()) {
            score = 1500;
        } else if (isStraight()){
            score = 1000;
        }

        System.out.println(duplicates.size());

        score = switch(duplicates.size()) {
            case 6 -> 750;
            case 4 -> 500;
            case 3 -> 80;
            case 2 -> 30;
            case 1 -> 10;
            default -> score;
        };

        return score;
    }

    private List<Integer> findDuplicates(Hand hand) {
        List<Integer> duplicates = new ArrayList<>();
        int[] numbers = new int[5];
        for (int i = 0; i < 5; i++) {
            numbers[i] = hand.getHand().get(i).getValue();
        }
        System.out.println(Arrays.toString(numbers));
        for (int i = 0; i < numbers.length; i++) {
            for (int j = i; j < numbers.length - 1; j++) {
                if (numbers[i] == numbers[j + 1]) {
                    duplicates.add(numbers[i]);

                }
            }
        }
        return duplicates;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public void setDuplicates(Hand hand) {
        duplicates = findDuplicates(hand);
    }

}
