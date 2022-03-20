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

import p.lodz.pl.model.exceptions.UnspecifiedLevelException;

public class Level {
  private Difficulty difficulty;

  public Level(String difficultyLevel) throws UnspecifiedLevelException {
    difficulty = matchDifficulty(difficultyLevel);
  }

  Difficulty matchDifficulty(String difficultyLevel) throws UnspecifiedLevelException {
    Difficulty answer;
    switch (difficultyLevel) {
    case "easy" -> answer = new EasyDifficulty();
    case "medium" -> answer = new MediumDifficulty();
    case "hard" -> answer = new HardDifficulty();
    default -> throw new UnspecifiedLevelException(difficultyLevel);
    }
    return answer;
  }

  public SudokuBoard prepareBoard() {
    SudokuBoard board = SudokuBoardPrototypeFactory.create();
    board.solveGame();
    difficulty.removeFields(board);
    board.makeEmptyFieldsEditable();
    return board;
  }
}
