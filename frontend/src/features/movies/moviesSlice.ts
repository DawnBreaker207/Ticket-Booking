
import { createSlice } from '@reduxjs/toolkit';
import type { PayloadAction } from '@reduxjs/toolkit';
import type { Movie } from '../../types/movie';

interface MoviesState {
    items: Movie[];
}

const initialState: MoviesState = {
    items: [],
};

const moviesSlice = createSlice({
    name: 'movies',
    initialState,
    reducers: {
        setMovies(state, action: PayloadAction<Movie[]>) {
            state.items = action.payload;
        },
        addMovie(state, action: PayloadAction<Movie>) {
            state.items.push(action.payload);
        },
        updateMovie(state, action: PayloadAction<Movie>) {
            state.items = state.items.map(i => (i.id === action.payload.id ? action.payload : i));
        },
        removeMovie(state, action: PayloadAction<string>) {
            state.items = state.items.filter(i => i.id !== action.payload);
        },
        clearAll(state) {
            state.items = [];
        },
    },
});

export const { setMovies, addMovie, updateMovie, removeMovie, clearAll } = moviesSlice.actions;
export default moviesSlice.reducer;
