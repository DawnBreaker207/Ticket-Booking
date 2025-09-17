import { configureStore } from '@reduxjs/toolkit';
import moviesReducer from '../features/movies/moviesSlice';
import cinemaHallsReducer from '../features/cinemaHalls/cinemaHallsSlice';
import authReducer from "../features/auth/authSlice";
export const store = configureStore({
  reducer: {
    movies: moviesReducer,
    cinemaHalls: cinemaHallsReducer,
    auth: authReducer, 
    
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
