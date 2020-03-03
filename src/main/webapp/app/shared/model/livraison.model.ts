import { Moment } from 'moment';
import { IConvoi } from 'app/shared/model/convoi.model';
import { IClient } from 'app/shared/model/client.model';
import { StatutLivraison } from 'app/shared/model/enumerations/statut-livraison.model';

export interface ILivraison {
  id?: number;
  code?: string;
  poids?: number;
  datelivraison?: Moment;
  status?: StatutLivraison;
  convoi?: IConvoi;
  client?: IClient;
}

export const defaultValue: Readonly<ILivraison> = {};
