import { configureStore } from '@reduxjs/toolkit';
import moviesReducer from '../features/movies/moviesSlice';
import cinemaHallsReducer from '../features/cinemaHalls/cinemaHallsSlice';
export const store = configureStore({
  reducer: {
    movies: moviesReducer,
    cinemaHalls: cinemaHallsReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
