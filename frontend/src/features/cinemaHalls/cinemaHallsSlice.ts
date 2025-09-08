// src/features/cinemaHalls/cinemaHallsSlice.ts
import { createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';
import type { CinemaHall } from '../../types/CinemaHall';

type State = {
  items: CinemaHall[];
};

const initialState: State = {
  items: [],
};

const slice = createSlice({
  name: 'cinemaHalls',
  initialState,
  reducers: {
    setCinemaHalls(state, action: PayloadAction<CinemaHall[]>) {
      state.items = action.payload;
    },
    addCinemaHall(state, action: PayloadAction<CinemaHall>) {
      // thêm lên đầu (giữ nguyên hành vi trước)
      state.items.unshift(action.payload);
    },
    updateCinemaHall(state, action: PayloadAction<CinemaHall>) {
      const idx = state.items.findIndex((it) => it.id === action.payload.id);
      if (idx !== -1) {
        state.items[idx] = action.payload;
      }
    },
    removeCinemaHall(state, action: PayloadAction<number>) {
      state.items = state.items.filter((it) => it.id !== action.payload);
    },
    clearCinemaHalls(state) {
      state.items = [];
    },
  },
});

export const {
  setCinemaHalls,
  addCinemaHall,
  updateCinemaHall,
  removeCinemaHall,
  clearCinemaHalls,
} = slice.actions;

export default slice.reducer;
