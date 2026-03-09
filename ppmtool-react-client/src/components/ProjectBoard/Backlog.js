import React, { Component } from "react";
import ProjectTask from "./ProjectTasks/ProjectTask";

class Backlog extends Component {
  render() {
    const { project_tasks_prop } = this.props;
    return (
      <div>
        {project_tasks_prop.map(pt => (
          <ProjectTask key={pt.id} project_task={pt} />
        ))}
      </div>
    );
  }
}

export default Backlog;
