import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Livraison from './livraison';
import LivraisonDetail from './livraison-detail';
import LivraisonUpdate from './livraison-update';
import LivraisonDeleteDialog from './livraison-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LivraisonDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LivraisonUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LivraisonUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LivraisonDetail} />
      <ErrorBoundaryRoute path={match.url} component={Livraison} />
    </Switch>
  </>
);

export default Routes;
