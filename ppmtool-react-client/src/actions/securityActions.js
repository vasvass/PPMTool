import axios from "axios";
import { GET_ERRORS, SET_CURRENT_USER } from "./types";

const TOKEN_KEY = "jwtToken";

const setJWTToken = token => {
  if (token) {
    axios.defaults.headers.common["Authorization"] = token;
    localStorage.setItem(TOKEN_KEY, token);
  } else {
    delete axios.defaults.headers.common["Authorization"];
    localStorage.removeItem(TOKEN_KEY);
  }
};

const decodeToken = token => {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    return JSON.parse(window.atob(base64));
  } catch (e) {
    return {};
  }
};

export const login = (LoginRequest, history) => async dispatch => {
  try {
    const res = await axios.post("/api/users/login", LoginRequest);
    const { token } = res.data;
    setJWTToken(token);
    const decoded = decodeToken(token);
    dispatch({
      type: SET_CURRENT_USER,
      payload: decoded
    });
    history.push("/dashboard");
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data
    });
  }
};

export const register = (newUser, history) => async dispatch => {
  try {
    await axios.post("/api/users/register", newUser);
    history.push("/login");
    dispatch({
      type: GET_ERRORS,
      payload: {}
    });
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data
    });
  }
};

export const logout = () => dispatch => {
  setJWTToken(null);
  dispatch({
    type: SET_CURRENT_USER,
    payload: {}
  });
};
