import React, { Component } from "react";
//import logo from "./logo.svg";
import "./App.css";
import Dashboard from "./components/Dashboard";
import Header from "./components/Layout/Header";
import Landing from "./components/Layout/Landing";
import "bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import AddProject from "./components/Project/AddProject";
import { Provider } from "react-redux";
import store from "./store";
import UpdateProject from "./components/Project/UpdateProject";
import ProjectBoard from "./components/ProjectBoard/ProjectBoard";
import AddProjectTask from "./components/ProjectBoard/ProjectTasks/AddProjectTask";
import UpdateProjectTask from "./components/ProjectBoard/ProjectTasks/UpdateProjectTask";
import { SET_CURRENT_USER } from "./actions/types";
import SecuredRoute from "./securityUtils/SecuredRoute";
import Login from "./components/UserManagement/Login";
import Register from "./components/UserManagement/Register";

const TOKEN_KEY = "jwtToken";

const decodeToken = token => {
  try {
    const base64Url = token.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    return JSON.parse(window.atob(base64));
  } catch (e) {
    return {};
  }
};

const token = localStorage.getItem(TOKEN_KEY);
if (token) {
  const decoded = decodeToken(token);
  const currentTime = Date.now() / 1000;
  if (decoded.exp > currentTime) {
    const axios = require("axios");
    axios.defaults.headers.common["Authorization"] = token;
    store.dispatch({
      type: SET_CURRENT_USER,
      payload: decoded
    });
  } else {
    localStorage.removeItem(TOKEN_KEY);
    delete require("axios").defaults.headers.common["Authorization"];
  }
}

class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <Router>
          <div className="App">
            <Header />
            <Switch>
              <Route exact path="/" component={Landing} />
              <Route exact path="/register" component={Register} />
              <Route exact path="/login" component={Login} />
              <SecuredRoute exact path="/dashboard" component={Dashboard} />
              <SecuredRoute exact path="/addProject" component={AddProject} />
              <SecuredRoute exact path="/updateProject/:id" component={UpdateProject} />
              <SecuredRoute exact path="/projectBoard/:id" component={ProjectBoard} />
              <SecuredRoute exact path="/addProjectTask/:id" component={AddProjectTask} />
              <SecuredRoute exact path="/updateProjectTask/:backlog_id/:pt_id" component={UpdateProjectTask} />
            </Switch>
          </div>
        </Router>
      </Provider>
    );
  }
}
export default App;
