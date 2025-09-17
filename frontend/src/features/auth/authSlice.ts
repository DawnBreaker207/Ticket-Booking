// src/features/auth/authSlice.ts
import  { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from '@reduxjs/toolkit';
import * as authService from "../../services/authService";


interface AuthState {
  user: any | null;
  token: string | null;
  status: "idle" | "loading" | "succeeded" | "failed";
  error: string | null;
}

const initialState: AuthState = {
  user: null,
  token: typeof window !== "undefined" ? localStorage.getItem("token") : null,
  status: "idle",
  error: null,
};

// helper đơn giản lấy message từ axios/other errors
const extractErrorMessage = (err: any) => {
  if (err?.response?.data?.message) return err.response.data.message;
  if (err?.message) return err.message;
  return "Lỗi không xác định";
};

// thunk đăng nhập
export const loginUser = createAsyncThunk(
  "auth/login",
  async (payload: { email: string; password: string }, thunkAPI) => {
    try {
      const res = await authService.login(payload);
      return res;
    } catch (err) {
      return thunkAPI.rejectWithValue(extractErrorMessage(err));
    }
  }
);

// thunk đăng ký
export const registerUser = createAsyncThunk(
  "auth/register",
  async (payload: authService.RegisterPayload, thunkAPI) => {
    try {
      const res = await authService.register(payload);
      return res;
    } catch (err) {
      return thunkAPI.rejectWithValue(extractErrorMessage(err));
    }
  }
);

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    logoutLocal(state) {
      state.user = null;
      state.token = null;
      state.status = "idle";
      state.error = null;
      authService.logout();
    },
    setUser(state, action: PayloadAction<{ user: any; token?: string }>) {
      state.user = action.payload.user;
      if (action.payload.token) {
        state.token = action.payload.token;
        localStorage.setItem("token", action.payload.token);
        authService.setAuthToken(action.payload.token);
      }
    },
  },
  extraReducers: (builder) => {
    // login
    builder.addCase(loginUser.pending, (state) => {
      state.status = "loading";
      state.error = null;
    });
    builder.addCase(loginUser.fulfilled, (state, action) => {
      state.status = "succeeded";
      state.error = null;
      // nếu API trả token/user
      if (action.payload?.token) {
        state.token = action.payload.token;
        state.user = action.payload.user ?? null;
      } else {
        // có thể API trả message thôi => lưu nếu cần
      }
    });
    builder.addCase(loginUser.rejected, (state, action) => {
      state.status = "failed";
      state.error = action.payload as string;
    });

    // register
    builder.addCase(registerUser.pending, (state) => {
      state.status = "loading";
      state.error = null;
    });
    builder.addCase(registerUser.fulfilled, (state, action) => {
      state.status = "succeeded";
      state.error = null;
      // một số API trả token ngay sau register — xử lý nếu có
      if (action.payload?.token) {
        state.token = action.payload.token;
        state.user = action.payload.user ?? null;
      }
    });
    builder.addCase(registerUser.rejected, (state, action) => {
      state.status = "failed";
      state.error = action.payload as string;
    });
  },
});

export const { logoutLocal, setUser } = authSlice.actions;
export default authSlice.reducer;

// selector ví dụ

