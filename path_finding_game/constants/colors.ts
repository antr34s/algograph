import { CellType } from '../types/grid';

export const CELL_COLORS: Record<CellType, string> = {
  empty: '#05010a',
  start: '#00ffcc',
  end: '#ff0055',
  obstacle: '#fff',
  visited: '#0077ff',
  path: '#ffff00',
};