import React, { Component } from "react";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { Link } from "react-router-dom";
import { getBacklog } from "../../actions/backlogActions";
import Backlog from "./Backlog";

class ProjectBoard extends Component {
  componentDidMount() {
    const { id } = this.props.match.params;
    this.props.getBacklog(id);
  }

  render() {
    const { project_tasks } = this.props.backlog;
    const { id } = this.props.match.params;

    const tasks = project_tasks || [];
    const todos = tasks.filter(pt => pt.status === "TO_DO");
    const inProgress = tasks.filter(pt => pt.status === "IN_PROGRESS");
    const done = tasks.filter(pt => pt.status === "DONE");

    return (
      <div className="container">
        <Link to={`/addProjectTask/${id}`} className="btn btn-primary mb-3">
          <i className="fa fa-plus-circle" /> Create Project Task
        </Link>
        <br />
        <hr />
        <div className="row">
          <div className="col-md-4">
            <div className="card text-center mb-3">
              <div className="card-header bg-secondary text-white">
                <h3>TO DO</h3>
              </div>
            </div>
            <Backlog project_tasks_prop={todos} />
          </div>
          <div className="col-md-4">
            <div className="card text-center mb-3">
              <div className="card-header bg-primary text-white">
                <h3>IN PROGRESS</h3>
              </div>
            </div>
            <Backlog project_tasks_prop={inProgress} />
          </div>
          <div className="col-md-4">
            <div className="card text-center mb-3">
              <div className="card-header bg-success text-white">
                <h3>DONE</h3>
              </div>
            </div>
            <Backlog project_tasks_prop={done} />
          </div>
        </div>
      </div>
    );
  }
}

ProjectBoard.propTypes = {
  backlog: PropTypes.object.isRequired,
  getBacklog: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
  backlog: state.backlog
});

export default connect(
  mapStateToProps,
  { getBacklog }
)(ProjectBoard);
