import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Convoi from './convoi';
import Livraison from './livraison';
import Client from './client';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}convoi`} component={Convoi} />
      <ErrorBoundaryRoute path={`${match.url}livraison`} component={Livraison} />
      <ErrorBoundaryRoute path={`${match.url}client`} component={Client} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
