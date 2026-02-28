import { Cell } from '../types/grid';

const DEFAULT_WEIGHT = 1;

export const GRID_SIZE_LARGE = 30;
export const GRID_SIZE_SMALL = 10;

export const createGrid = (gridSize: number = GRID_SIZE_LARGE): Cell[][] => {
  return Array.from({ length: gridSize }, (_, row) =>
    Array.from({ length: gridSize }, (_, col) => ({
      row,
      col,
      type: 'empty',
      weight: DEFAULT_WEIGHT,
    }))
  );
};