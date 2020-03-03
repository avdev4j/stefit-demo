import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IConvoi, defaultValue } from 'app/shared/model/convoi.model';

export const ACTION_TYPES = {
  FETCH_CONVOI_LIST: 'convoi/FETCH_CONVOI_LIST',
  FETCH_CONVOI: 'convoi/FETCH_CONVOI',
  CREATE_CONVOI: 'convoi/CREATE_CONVOI',
  UPDATE_CONVOI: 'convoi/UPDATE_CONVOI',
  DELETE_CONVOI: 'convoi/DELETE_CONVOI',
  RESET: 'convoi/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IConvoi>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ConvoiState = Readonly<typeof initialState>;

// Reducer

export default (state: ConvoiState = initialState, action): ConvoiState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CONVOI_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CONVOI):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CONVOI):
    case REQUEST(ACTION_TYPES.UPDATE_CONVOI):
    case REQUEST(ACTION_TYPES.DELETE_CONVOI):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CONVOI_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CONVOI):
    case FAILURE(ACTION_TYPES.CREATE_CONVOI):
    case FAILURE(ACTION_TYPES.UPDATE_CONVOI):
    case FAILURE(ACTION_TYPES.DELETE_CONVOI):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONVOI_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONVOI):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CONVOI):
    case SUCCESS(ACTION_TYPES.UPDATE_CONVOI):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CONVOI):
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

const apiUrl = 'api/convois';

// Actions

export const getEntities: ICrudGetAllAction<IConvoi> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CONVOI_LIST,
    payload: axios.get<IConvoi>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IConvoi> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CONVOI,
    payload: axios.get<IConvoi>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IConvoi> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CONVOI,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IConvoi> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CONVOI,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IConvoi> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CONVOI,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
