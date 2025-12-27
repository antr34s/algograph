import { Cell } from '../types/grid';

export const GRID_SIZE = 30;

export const createGrid = (): Cell[][] => {
  return Array.from({ length: GRID_SIZE }, (_, row) =>
    Array.from({ length: GRID_SIZE }, (_, col) => ({
      row,
      col,
      type: 'empty',
    }))
  );
};