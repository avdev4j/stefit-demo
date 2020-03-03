import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './livraison.reducer';
import { ILivraison } from 'app/shared/model/livraison.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ILivraisonDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LivraisonDetail = (props: ILivraisonDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { livraisonEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="stefitApp.livraison.detail.title">Livraison</Translate> [<b>{livraisonEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="code">
              <Translate contentKey="stefitApp.livraison.code">Code</Translate>
            </span>
          </dt>
          <dd>{livraisonEntity.code}</dd>
          <dt>
            <span id="poids">
              <Translate contentKey="stefitApp.livraison.poids">Poids</Translate>
            </span>
          </dt>
          <dd>{livraisonEntity.poids}</dd>
          <dt>
            <span id="datelivraison">
              <Translate contentKey="stefitApp.livraison.datelivraison">Datelivraison</Translate>
            </span>
          </dt>
          <dd>
            <TextFormat value={livraisonEntity.datelivraison} type="date" format={APP_LOCAL_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="stefitApp.livraison.status">Status</Translate>
            </span>
          </dt>
          <dd>{livraisonEntity.status}</dd>
          <dt>
            <Translate contentKey="stefitApp.livraison.convoi">Convoi</Translate>
          </dt>
          <dd>{livraisonEntity.convoi ? livraisonEntity.convoi.label : ''}</dd>
          <dt>
            <Translate contentKey="stefitApp.livraison.client">Client</Translate>
          </dt>
          <dd>{livraisonEntity.client ? livraisonEntity.client.nom : ''}</dd>
        </dl>
        <Button tag={Link} to="/livraison" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/livraison/${livraisonEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ livraison }: IRootState) => ({
  livraisonEntity: livraison.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LivraisonDetail);
