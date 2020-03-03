import { ILivraison } from 'app/shared/model/livraison.model';

export interface IConvoi {
  id?: number;
  label?: string;
  livraisons?: ILivraison[];
}

export const defaultValue: Readonly<IConvoi> = {};
