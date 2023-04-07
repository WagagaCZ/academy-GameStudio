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

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
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
        switch (duplicates.size()) {
            // Four of a kind
            case 6 -> {
                score = 750;
            }
            // Three of a kind + one pair = full house
            case 4 -> {
                score = 500;
            }
            //Three of a kind - one duplicate gets twice into the list, the other one once

            case 3 -> {
                score = 80;
            }
            //Two pair
            case 2 -> {
                score = 30;
            }
            //One pair
            case 1 -> {
                score = 10;
            }


        }
        return score;
    }

    public void setDuplicates(Hand hand) {
        duplicates = findDuplicates(hand);
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

}
