import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILivraison, defaultValue } from 'app/shared/model/livraison.model';

export const ACTION_TYPES = {
  FETCH_LIVRAISON_LIST: 'livraison/FETCH_LIVRAISON_LIST',
  FETCH_LIVRAISON: 'livraison/FETCH_LIVRAISON',
  CREATE_LIVRAISON: 'livraison/CREATE_LIVRAISON',
  UPDATE_LIVRAISON: 'livraison/UPDATE_LIVRAISON',
  DELETE_LIVRAISON: 'livraison/DELETE_LIVRAISON',
  RESET: 'livraison/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILivraison>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type LivraisonState = Readonly<typeof initialState>;

// Reducer

export default (state: LivraisonState = initialState, action): LivraisonState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_LIVRAISON_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LIVRAISON):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_LIVRAISON):
    case REQUEST(ACTION_TYPES.UPDATE_LIVRAISON):
    case REQUEST(ACTION_TYPES.DELETE_LIVRAISON):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_LIVRAISON_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LIVRAISON):
    case FAILURE(ACTION_TYPES.CREATE_LIVRAISON):
    case FAILURE(ACTION_TYPES.UPDATE_LIVRAISON):
    case FAILURE(ACTION_TYPES.DELETE_LIVRAISON):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_LIVRAISON_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_LIVRAISON):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_LIVRAISON):
    case SUCCESS(ACTION_TYPES.UPDATE_LIVRAISON):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_LIVRAISON):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/livraisons';

// Actions

export const getEntities: ICrudGetAllAction<ILivraison> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_LIVRAISON_LIST,
    payload: axios.get<ILivraison>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ILivraison> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LIVRAISON,
    payload: axios.get<ILivraison>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ILivraison> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LIVRAISON,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<ILivraison> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LIVRAISON,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILivraison> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LIVRAISON,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
