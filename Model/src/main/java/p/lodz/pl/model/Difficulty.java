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

class Difficulty {
    int amountOfFieldsToRemove = 0;

    public Difficulty() {
    }

    public void removeFields(SudokuBoard board) {
        int objective = amountOfFieldsToRemove;

        while (objective > 0) {
            int x = (int) (Math.random() * 9);
            int y = (int) (Math.random() * 9);
            if (board.get(x, y) != 0) {
                board.set(x, y, 0);
                objective--;
            }
        }
    }

}
