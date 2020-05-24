package com.bbh.LedControllerApi.forms;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TicEvent {
    private String mqttClientId;
    private Integer gameId;
    private Integer boardId;
    private Integer playerId1;
    private Integer playerId2;
    private Integer nextTurn;
    private Character[][] positions;
    private Integer winner;
    private boolean start;
    private boolean reset;
    private Date gameCreationTime;
    private Date boardCreationTime;
    private Date lastBoardUpdateTime;


    public String getMqttClientId() {
        return mqttClientId;
    }

    public void setMqttClientId(String mqttClientId) {
        this.mqttClientId = mqttClientId;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public Integer getBoardId() {
        return boardId;
    }

    public void setBoardId(Integer boardId) {
        this.boardId = boardId;
    }

    public Integer getPlayerId1() {
        return playerId1;
    }

    public void setPlayerId1(Integer playerId1) {
        this.playerId1 = playerId1;
    }

    public Integer getPlayerId2() {
        return playerId2;
    }

    public void setPlayerId2(Integer playerId2) {
        this.playerId2 = playerId2;
    }

    public Integer getNextTurn() {
        return nextTurn;
    }

    public void setNextTurn(Integer nextTurn) {
        this.nextTurn = nextTurn;
    }

    public Character[][] getPositions() {
        return positions;
    }

    public void setPositions(Character[][] positions) {
        this.positions = positions;
    }

    public Integer getWinner() {
        return winner;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public Date getGameCreationTime() {
        return gameCreationTime;
    }

    public void setGameCreationTime(Date gameCreationTime) {
        this.gameCreationTime = gameCreationTime;
    }

    public Date getBoardCreationTime() {
        return boardCreationTime;
    }

    public void setBoardCreationTime(Date boardCreationTime) {
        this.boardCreationTime = boardCreationTime;
    }

    public Date getLastBoardUpdateTime() {
        return lastBoardUpdateTime;
    }

    public void setLastBoardUpdateTime(Date lastBoardUpdateTime) {
        this.lastBoardUpdateTime = lastBoardUpdateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
