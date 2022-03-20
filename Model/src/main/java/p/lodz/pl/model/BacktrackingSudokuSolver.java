//    sudoku thing; projekt na zajęcia z PROKOM 2021-2022
//    Copyright (C) 2021  Antoni Jończyk, Wiktor Pawlak
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU Affero General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU Affero General Public License for more details.
//
//    You should have received a copy of the GNU Affero General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.

package p.lodz.pl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BacktrackingSudokuSolver implements ISudokuSolver, Serializable {

  private final int size = 9;
  private SudokuBoard board;
  private Positions remainingPositions;

  static class Position {
    private final int cx;
    private final int cy;

    Position(int x, int y) {
      this.cx = x;
      this.cy = y;
    }
  }

  private class Positions {
    private final List<Position> positionList;
    private int current = 0;

    Positions(SudokuBoard board) {
      positionList = new ArrayList<>();
      for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
          if (board.get(i, j) == 0) {
            positionList.add(new Position(i, j));
          }
        }
      }
    }

    public Position next() {
      return positionList.get(current++);
    }

    public void back() {
      current--;
    }

    public boolean isSudokuComplete() {
      return current == positionList.size(); // testy przechodzą
    }
  }

  @Override
  public boolean solve(SudokuBoard board) {
    this.board = board;
    remainingPositions = new Positions(board);
    return solveBacktracking();
  }

  private boolean solveBacktracking() {
    if (remainingPositions.isSudokuComplete()) {
      return true;
    }
    Position pos = remainingPositions.next();

    for (int choice : getPossibleChoices()) {
      if (willItFit(pos, choice)) {
        board.set(pos.cx, pos.cy, choice);
        if (solveBacktracking()) {
          return true;
        }
      }
    }
    board.set(pos.cx, pos.cy, 0);
    remainingPositions.back();
    return false;
  }

  private List<Integer> getPossibleChoices() {
    List<Integer> possibleChoices = IntStream.range(1, 10).boxed().collect(Collectors.toList());
    Collections.shuffle(possibleChoices);
    return possibleChoices;
  }

  private boolean willItFit(Position pos, int num) {
    if (board.getBox(pos.cy, pos.cx).contains(num)) {
      return false;
    }
    if (board.getRow(pos.cx).contains(num)) {
      return false;
    }
    if (board.getColumn(pos.cy).contains(num)) {
      return false;
    }
    return true;
  }
}
