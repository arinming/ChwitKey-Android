import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cherry_pick_android.data.model.SearchRecordEntity
import com.example.cherry_pick_android.databinding.ItemSearchBinding
import com.example.cherry_pick_android.presentation.ui.keyword.DeleteListener

class SearchRecordAdapter(
    private val deleteListener: DeleteListener
    ): RecyclerView.Adapter<SearchRecordAdapter.ViewHolder>() {
    private val records = ArrayList<SearchRecordEntity>()

    // 뷰 홀더 클래스
    inner class ViewHolder(val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setRecordItem(records: String) {
            binding.btnRecentSearchItem.text = records
        }

        init {
            // 삭제 버튼 클릭 이벤트 설정
            binding.ibtnDeleteRecentSearch.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    deleteListener.onDeleteClick(records[position].record)
                }
            }
        }
    }

    // 아이템 레이아웃 호출
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false
        )
        return ViewHolder(binding)
    }

    // 호출한 내용으로 bind
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.setRecordItem(records[position].record)

        // 삭제 버튼 클릭 이벤트 설정
        viewHolder.binding.ibtnDeleteRecentSearch.setOnClickListener {
            deleteListener.onDeleteClick(records[position].record)
        }
    }

    // 데이터 크기 반환
    override fun getItemCount() = records.size

    fun setList(list: List<SearchRecordEntity>) {
        records.clear()
        records.addAll(list.reversed())
        notifyDataSetChanged()
    }

    fun getRecords(): List<SearchRecordEntity> {
        return records
    }

    fun removeRecord(record: SearchRecordEntity) {
        val position = records.indexOf(record)
        if (position != -1) {
            records.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }
}