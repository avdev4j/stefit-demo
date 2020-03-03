import { ILivraison } from 'app/shared/model/livraison.model';

export interface IClient {
  id?: number;
  nom?: string;
  adresse?: string;
  postalCode?: string;
  city?: string;
  livraisons?: ILivraison[];
}

export const defaultValue: Readonly<IClient> = {};
