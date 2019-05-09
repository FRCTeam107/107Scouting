package com.frc107.scouting.cycle;

import com.frc107.scouting.R;
import com.frc107.scouting.Scouting;
import com.frc107.scouting.model.BaseModel;
import com.frc107.scouting.model.question.Question;
import com.frc107.scouting.model.question.RadioQuestion;
import com.frc107.scouting.model.question.ToggleQuestion;

public class CycleModel extends BaseModel {
    private int cycleNum;
    private boolean hasUsedStartingItem;
    private int teamNumber;

    public CycleModel(int teamNumber, boolean hasUsedStartingItem) {
        super();
        this.teamNumber = teamNumber;
        this.hasUsedStartingItem = !hasUsedStartingItem;
    }

    @Override
    public Question[] getQuestions() {
        ToggleQuestion allDefenseQuestion = new ToggleQuestion("cycleAllDefense", R.id.allDefense_chkbx);
        allDefenseQuestion.setIgnoreAnswer(true);

        Question[] questions = {
                new RadioQuestion("cyclePickupLoc", R.id.pickupLocationRadioQuestion, true,
                        new RadioQuestion.Option(R.id.portPickupLocation_Radiobtn, Scouting.CYCLE_PORT_PICKUP),
                        new RadioQuestion.Option(R.id.floorPickupLocation_Radiobtn, Scouting.CYCLE_FLOOR_PICKUP),
                        new RadioQuestion.Option(R.id.startedWithItem_Radiobtn, Scouting.CYCLE_STARTED_WITH_ITEM)),
                new RadioQuestion("cycleItemPickedUp", R.id.itemPickedUpRadioQuestion, true,
                        new RadioQuestion.Option(R.id.cargoItemPickedUp_Radiobtn, Scouting.CYCLE_CARGO),
                        new RadioQuestion.Option(R.id.hatchItemPickedUp_Radiobtn, Scouting.CYCLE_PANEL)),
                new RadioQuestion("cycleItemPlaced", R.id.itemPlacedRadioQuestion, true,
                        new RadioQuestion.Option(R.id.topRocketItemPlaced_Radiobtn, Scouting.CYCLE_TOP_ROCKET),
                        new RadioQuestion.Option(R.id.middleRocketItemPlaced_Radiobtn, Scouting.CYCLE_MIDDLE_ROCKET),
                        new RadioQuestion.Option(R.id.bottomRocketItemPlaced_Radiobtn, Scouting.CYCLE_BOTTOM_ROCKET),
                        new RadioQuestion.Option(R.id.cargoshipItemPlaced_Radiobtn, Scouting.CYCLE_CARGO_SHIP),
                        new RadioQuestion.Option(R.id.floorItemPlaced_Radiobtn, Scouting.CYCLE_FLOOR),
                        new RadioQuestion.Option(R.id.nothingPlacedItemPlaced_Radiobtn, Scouting.CYCLE_NOTHING_PLACED)),
                new ToggleQuestion("cycleDefense", R.id.defense_chkbx),
                allDefenseQuestion
        };
        return questions;
    }

    @Override
    public void onNumberQuestionAnswered(int questionId, Integer answer) { }

    @Override
    public void onTextQuestionAnswered(int questionId, String answer) { }

    @Override
    public void onRadioQuestionAnswered(int questionId, int answerId) {
        if (questionId == R.id.pickupLocationRadioQuestion) {
            if (answerId == R.id.startedWithItem_Radiobtn) {
                hasUsedStartingItem = true;
            } else {
                hasUsedStartingItem = false;
            }
        }
    }

    private void saveCycle() {
        // TODO: This would be cleaner if you:
        // 1: renamed Questions to Fields
        // 2: for type and cycleNum, made a kind of Field that's just called VariableField or something, something that's just controlled in-code, not outside of code
        String csvRow = getAnswerCSVRow();
        String cycle = cycleNum + "," + csvRow;

        if (Scouting.SAVE_QUESTION_NAMES_AS_ANSWERS) {
            cycle = "cycleNum," + csvRow;
        }

        Scouting.getInstance().addCycle(cycle);
    }

    public void setAllDefense(boolean allDefense) {
        Question pickupLocationQuestion = getQuestion(R.id.pickupLocationRadioQuestion);
        Question itemPickedUpQuestion = getQuestion(R.id.itemPickedUpRadioQuestion);
        Question itemPlacedRadioQuestion = getQuestion(R.id.itemPlacedRadioQuestion);

        boolean needsAnswers = !allDefense;
        pickupLocationQuestion.setNeedsAnswer(needsAnswers);
        itemPickedUpQuestion.setNeedsAnswer(needsAnswers);
        itemPlacedRadioQuestion.setNeedsAnswer(needsAnswers);
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || areNoQuestionsAnswered();
    }

    @Override
    public boolean finish() {
        if (getUnfinishedQuestionId() == -1) {
            cycleNum++;
            saveCycle();
        }
        return true;
    }

    public boolean hasUsedStartingItem() {
        return hasUsedStartingItem;
    }

    public int getTeamNumber() {
        return teamNumber;
    }
}
