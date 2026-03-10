import { SET_CURRENT_USER } from "../actions/types";

const initialState = {
  validToken: false,
  user: {}
};

const booleanActionPayload = payload => {
  if (payload !== null && typeof payload === "object") {
    return Object.keys(payload).length > 0;
  }
  return false;
};

export default function(state = initialState, action) {
  switch (action.type) {
    case SET_CURRENT_USER:
      return {
        ...state,
        validToken: booleanActionPayload(action.payload),
        user: action.payload
      };

    default:
      return state;
  }
}
