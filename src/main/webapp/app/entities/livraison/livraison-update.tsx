import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IConvoi } from 'app/shared/model/convoi.model';
import { getEntities as getConvois } from 'app/entities/convoi/convoi.reducer';
import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { getEntity, updateEntity, createEntity, reset } from './livraison.reducer';
import { ILivraison } from 'app/shared/model/livraison.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILivraisonUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LivraisonUpdate = (props: ILivraisonUpdateProps) => {
  const [convoiId, setConvoiId] = useState('0');
  const [clientId, setClientId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { livraisonEntity, convois, clients, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/livraison');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getConvois();
    props.getClients();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...livraisonEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="stefitApp.livraison.home.createOrEditLabel">
            <Translate contentKey="stefitApp.livraison.home.createOrEditLabel">Create or edit a Livraison</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : livraisonEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="livraison-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="livraison-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="codeLabel" for="livraison-code">
                  <Translate contentKey="stefitApp.livraison.code">Code</Translate>
                </Label>
                <AvField id="livraison-code" type="text" name="code" />
              </AvGroup>
              <AvGroup>
                <Label id="poidsLabel" for="livraison-poids">
                  <Translate contentKey="stefitApp.livraison.poids">Poids</Translate>
                </Label>
                <AvField id="livraison-poids" type="string" className="form-control" name="poids" />
              </AvGroup>
              <AvGroup>
                <Label id="datelivraisonLabel" for="livraison-datelivraison">
                  <Translate contentKey="stefitApp.livraison.datelivraison">Datelivraison</Translate>
                </Label>
                <AvField id="livraison-datelivraison" type="date" className="form-control" name="datelivraison" />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="livraison-status">
                  <Translate contentKey="stefitApp.livraison.status">Status</Translate>
                </Label>
                <AvInput
                  id="livraison-status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && livraisonEntity.status) || 'EN_PREPARATION'}
                >
                  <option value="EN_PREPARATION">{translate('stefitApp.StatutLivraison.EN_PREPARATION')}</option>
                  <option value="PRET">{translate('stefitApp.StatutLivraison.PRET')}</option>
                  <option value="EXPEDIE">{translate('stefitApp.StatutLivraison.EXPEDIE')}</option>
                  <option value="RECU">{translate('stefitApp.StatutLivraison.RECU')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="livraison-convoi">
                  <Translate contentKey="stefitApp.livraison.convoi">Convoi</Translate>
                </Label>
                <AvInput id="livraison-convoi" type="select" className="form-control" name="convoi.id">
                  <option value="" key="0" />
                  {convois
                    ? convois.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.label}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="livraison-client">
                  <Translate contentKey="stefitApp.livraison.client">Client</Translate>
                </Label>
                <AvInput id="livraison-client" type="select" className="form-control" name="client.id">
                  <option value="" key="0" />
                  {clients
                    ? clients.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.nom}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/livraison" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  convois: storeState.convoi.entities,
  clients: storeState.client.entities,
  livraisonEntity: storeState.livraison.entity,
  loading: storeState.livraison.loading,
  updating: storeState.livraison.updating,
  updateSuccess: storeState.livraison.updateSuccess
});

const mapDispatchToProps = {
  getConvois,
  getClients,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LivraisonUpdate);
