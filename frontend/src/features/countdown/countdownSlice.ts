// src/features/countdown/countdownSlice.ts
import { createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';

interface CountdownState {
  remainingTime: number;
}

const initialState: CountdownState = {
  remainingTime: 0,
};

const countdownSlice = createSlice({
  name: "countdown",
  initialState,
  reducers: {
    updateCountdownTTL(state, action: PayloadAction<{ ttl: number }>) {
      state.remainingTime = action.payload.ttl;
    },
  },
});

export const { updateCountdownTTL } = countdownSlice.actions;
export const selectRemainingTime = (state: any) =>
  state.countdown?.remainingTime ?? 0;
export default countdownSlice.reducer;
