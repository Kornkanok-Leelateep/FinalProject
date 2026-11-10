package Duolearn01;

import java.io.Serializable;
import java.util.*;

public class AppData implements Serializable {
    private static final long serialVersionUID = 1L;

    // ArrayList: คลังคำศัพท์ทั้งหมด
    private final ArrayList<Word> words = new ArrayList<>();

    // HashMap: ค้นหาคำศัพท์จากคำภาษาอังกฤษได้รวดเร็ว
    private final HashMap<String, Word> wordMap = new HashMap<>();

    // TreeMap: จัดหมวดหมู่บทเรียนตามลำดับชื่อ (โครงสร้างต้นไม้แบบ Sorted Map)
    private final TreeMap<String, ArrayList<Word>> categories = new TreeMap<>();

    // Stack: เก็บประวัติคำศัพท์ที่เปิดเรียนล่าสุด
    private final Stack<Word> history = new Stack<>();

    // LinkedList: บันทึกกิจกรรมการเรียน
    private final LinkedList<String> activityLog = new LinkedList<>();

    // PriorityQueue: คำศัพท์ที่ต้องทบทวน โดยคำที่ผิดบ่อยจะมีลำดับก่อน
    private final PriorityQueue<ReviewItem> reviewQueue = new PriorityQueue<>();

    private int xp = 0;
    private int correctAnswers = 0;
    private int totalAnswers = 0;
    private int lessonsCompleted = 0;

    public AppData() {
        addWord(new Word("คำทักทาย", "Hello", "สวัสดี", "Hello! How are you?"));
        addWord(new Word("คำทักทาย", "Good morning", "อรุณสวัสดิ์", "Good morning, everyone."));
        addWord(new Word("คำทักทาย", "Thank you", "ขอบคุณ", "Thank you for your help."));
        addWord(new Word("คำทักทาย", "Goodbye", "ลาก่อน", "Goodbye. See you tomorrow."));
        addWord(new Word("คำทักทาย", "Please", "กรุณา", "Please sit down."));

        addWord(new Word("อาหาร", "Apple", "แอปเปิล", "I eat an apple."));
        addWord(new Word("อาหาร", "Bread", "ขนมปัง", "I would like some bread."));
        addWord(new Word("อาหาร", "Water", "น้ำ", "Can I have some water?"));
        addWord(new Word("อาหาร", "Rice", "ข้าว", "Rice is my favorite food."));
        addWord(new Word("อาหาร", "Milk", "นม", "I drink milk every morning."));

        addWord(new Word("สัตว์", "Cat", "แมว", "The cat is sleeping."));
        addWord(new Word("สัตว์", "Dog", "สุนัข", "The dog is friendly."));
        addWord(new Word("สัตว์", "Bird", "นก", "A bird can fly."));
        addWord(new Word("สัตว์", "Rabbit", "กระต่าย", "The rabbit is white."));
        addWord(new Word("สัตว์", "Fish", "ปลา", "Fish live in water."));

        addWord(new Word("สถานที่", "School", "โรงเรียน", "I go to school."));
        addWord(new Word("สถานที่", "Hospital", "โรงพยาบาล", "The hospital is near."));
        addWord(new Word("สถานที่", "Library", "ห้องสมุด", "The library is quiet."));
        addWord(new Word("สถานที่", "Market", "ตลาด", "My mother goes to the market."));
        addWord(new Word("สถานที่", "Airport", "สนามบิน", "We are at the airport."));
    }

    private void addWord(Word word) {
        words.add(word);
        wordMap.put(word.getEnglish().toLowerCase(Locale.ROOT), word);
        categories.computeIfAbsent(word.getCategory(), k -> new ArrayList<>()).add(word);
    }

    public ArrayList<Word> getWords() { return words; }
    public HashMap<String, Word> getWordMap() { return wordMap; }
    public TreeMap<String, ArrayList<Word>> getCategories() { return categories; }
    public Stack<Word> getHistory() { return history; }
    public LinkedList<String> getActivityLog() { return activityLog; }
    public PriorityQueue<ReviewItem> getReviewQueue() { return reviewQueue; }

    public int getXp() { return xp; }
    public int getCorrectAnswers() { return correctAnswers; }
    public int getTotalAnswers() { return totalAnswers; }
    public int getLessonsCompleted() { return lessonsCompleted; }

    public void recordAnswer(boolean correct) {
        totalAnswers++;
        if (correct) {
            correctAnswers++;
            xp += 10;
        }
    }

    public void addXp(int amount) { xp += amount; }
    public void lessonCompleted() { lessonsCompleted++; }

    public void log(String message) {
        activityLog.addFirst(message);
        while (activityLog.size() > 30) activityLog.removeLast();
    }

    public static class ReviewItem implements Serializable, Comparable<ReviewItem> {
        private static final long serialVersionUID = 1L;
        private final String english;
        private final int priority;
        public ReviewItem(String english, int priority) {
            this.english = english;
            this.priority = priority;
        }
        public String getEnglish() { return english; }
        public int getPriority() { return priority; }

        @Override
        public int compareTo(ReviewItem other) {
            return Integer.compare(this.priority, other.priority);
        }
    }
}
