import { Cell } from '../types/grid';

export const GRID_SIZE = 30;
const DEFAULT_WEIGHT = 1;
export const createGrid = (): Cell[][] => {
  return Array.from({ length: GRID_SIZE }, (_, row) =>
    Array.from({ length: GRID_SIZE }, (_, col) => ({
      row,
      col,
      type: 'empty',
      weight:DEFAULT_WEIGHT
    }))
  );
};