import { StyleSheet, Pressable } from 'react-native';
import { Cell as CellType } from '../types/grid';
import { CELL_COLORS } from '../constants/colors';

interface Props {
  cell: CellType;
  onPress: (cell: CellType) => void;
  onPressIn: () => void;
  onPressOut: () => void;
  isPressing: boolean;
}

export default function Cell({
  cell,
  onPress,
  onPressIn,
  onPressOut,
  isPressing,
}: Props) {
  return (
    <Pressable
      onPressIn={onPressIn}
      onPressOut={onPressOut}
      onPress={() => onPress(cell)}
      onHoverIn={() => {
        if (isPressing) {
          onPress(cell);
        }
      }}
      style={[
        styles.cell,
        { backgroundColor: CELL_COLORS[cell.type] },
      ]}
    />
  );
}

const styles = StyleSheet.create({
  cell: {
    width: 30,
    height: 30,
    borderWidth: 0.5,
    borderColor: '#00ffcc',
  },
});