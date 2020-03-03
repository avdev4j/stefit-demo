import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Convoi from './convoi';
import ConvoiDetail from './convoi-detail';
import ConvoiUpdate from './convoi-update';
import ConvoiDeleteDialog from './convoi-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ConvoiDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ConvoiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ConvoiUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ConvoiDetail} />
      <ErrorBoundaryRoute path={match.url} component={Convoi} />
    </Switch>
  </>
);

export default Routes;
